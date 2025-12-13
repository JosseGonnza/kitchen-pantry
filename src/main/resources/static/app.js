    const API = {
      products: "/api/products",
      summary: "/api/stock/summary",
      low: (threshold, category) => {
        const params = new URLSearchParams();
        params.set("threshold", String(threshold));
        if (category) params.set("category", category);
        return `/api/stock/low?${params.toString()}`;
      },
      batches: (name) => `/api/products/${encodeURIComponent(name)}/batches`,
      consume: (name) => `/api/products/${encodeURIComponent(name)}/consume`,
    };

    const el = {
      shelves: document.getElementById("shelves"),
      refreshBtn: document.getElementById("refreshBtn"),
      lastUpdated: document.getElementById("lastUpdated"),

      addProductForm: document.getElementById("addProductForm"),
      productName: document.getElementById("productName"),
      productCategory: document.getElementById("productCategory"),
      productMsg: document.getElementById("productMsg"),

      addBatchForm: document.getElementById("addBatchForm"),
      batchProductName: document.getElementById("batchProductName"),
      batchAmount: document.getElementById("batchAmount"),
      batchExpiry: document.getElementById("batchExpiry"),
      batchMsg: document.getElementById("batchMsg"),

      consumeForm: document.getElementById("consumeForm"),
      consumeProductName: document.getElementById("consumeProductName"),
      consumeAmount: document.getElementById("consumeAmount"),
      consumeMsg: document.getElementById("consumeMsg"),

      lowStockForm: document.getElementById("lowStockForm"),
      lowThreshold: document.getElementById("lowThreshold"),
      lowCategory: document.getElementById("lowCategory"),
      lowApplied: document.getElementById("lowApplied"),
      lowMsg: document.getElementById("lowMsg"),
      clearLowBtn: document.getElementById("clearLowBtn"),

      modalBackdrop: document.getElementById("modalBackdrop"),
      batchesModal: document.getElementById("batchesModal"),
      modalCloseBtn: document.getElementById("modalCloseBtn"),
      modalTitle: document.getElementById("modalTitle"),
      modalSubtitle: document.getElementById("modalSubtitle"),
      modalBody: document.getElementById("modalBody"),
    };

    // ---- UI helpers ----
    function setMsg(node, text, kind = "") {
      node.textContent = text ?? "";
      node.classList.remove("ok", "err");
      if (kind) node.classList.add(kind);
    }

    function nowTag() {
      const d = new Date();
      const pad = (n) => String(n).padStart(2, "0");
      return `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    }

    function escapeHtml(s) {
      return String(s)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
    }

    function toInt(v) {
      const n = Number(v);
      return Number.isFinite(n) ? n : NaN;
    }

    // ---- Robust request ----
    async function request(url, options) {
      const res = await fetch(url, options);

      const contentType = res.headers.get("content-type") || "";
      let body = null;

      try {
        if (contentType.includes("application/json")) {
          body = await res.json();
        } else {
          const text = await res.text();
          body = text ? { message: text } : null;
        }
      } catch {
        body = null;
      }

      if (res.ok) return { ok: true, res, body };
      return { ok: false, res, body };
    }

    function errorText({ res, body }) {
      const msg = body?.message ? String(body.message) : "Petición fallida";
      return `(${res.status}) ${msg}`;
    }

    // ---- Low stock state ----
    let lowFilter = { threshold: null, category: null };
    let lowSet = new Set();

    function setLowAppliedTag() {
      if (lowFilter.threshold === null) {
        el.lowApplied.textContent = "ninguno";
        return;
      }
      const c = lowFilter.category ? lowFilter.category : "Todas";
      el.lowApplied.textContent = `${c} <= ${lowFilter.threshold}`;
    }

    // ---- Data loading ----
    async function loadSummary() {
      const { ok, res, body } = await request(API.summary);
      if (!ok || !Array.isArray(body)) throw new Error(errorText({ res, body }));
      return body;
    }

    async function loadLowStock() {
      if (lowFilter.threshold === null) {
        lowSet = new Set();
        return;
      }
      const url = API.low(lowFilter.threshold, lowFilter.category);
      const { ok, res, body } = await request(url);
      if (!ok || !Array.isArray(body)) throw new Error(errorText({ res, body }));
      lowSet = new Set(body.map((x) => x.productName));
    }

    // ---- Render ----
    function groupByCategory(items) {
      const m = new Map();
      for (const it of items) {
        const key = it.category ?? "UNKNOWN";
        if (!m.has(key)) m.set(key, []);
        m.get(key).push(it);
      }
      return m;
    }

    function boxesHtml(qty) {
      const cap = 30;
      const capped = Math.min(qty, cap);
      let html = "";
      for (let i = 0; i < capped; i++) html += `<span class="box"></span>`;
      const rest = qty - capped;
      if (rest > 0) html += `<span class="badge">+${rest}</span>`;
      if (qty === 0) html += `<span class="badge">vacío</span>`;
      return html;
    }

    function productBadges(summary) {
      const batches = summary.numberOfBatches ?? 0;
      const next = summary.nextExpiryDate ?? "—";
      const parts = [];
      parts.push(`<span class="badge">${batches} lote${batches === 1 ? "" : "s"}</span>`);
      parts.push(`<span class="badge">próximo ${escapeHtml(next)}</span>`);
      if (lowSet.has(summary.productName)) {
        parts.push(`<span class="badge low">stock bajo</span>`);
      }
      return parts.join("");
    }

    function renderShelves(summary) {
      if (!summary.length) {
        el.shelves.innerHTML = `<p class="empty">No hay productos aún. Añade algunos.</p>`;
        return;
      }

      const grouped = groupByCategory(summary);
      const categories = Array.from(grouped.keys()).sort((a, b) => a.localeCompare(b));
      let html = "";

      for (const category of categories) {
        const items = grouped.get(category);
        items.sort((a, b) => a.productName.localeCompare(b.productName));

        let productsHtml = "";
        for (const p of items) {
          const low = lowSet.has(p.productName);
          productsHtml += `
            <div class="product-row ${low ? "low" : ""}">
              <div class="pname">
                <strong>${escapeHtml(p.productName)}</strong>
                <div class="pbadges">${productBadges(p)}</div>
              </div>

              <div class="boxes">${boxesHtml(p.totalQuantity ?? 0)}</div>

              <div class="actions">
                <button class="iconbtn" data-act="fill-batch" data-name="${escapeHtml(p.productName)}">+ Lote</button>
                <button class="iconbtn" data-act="fill-consume" data-name="${escapeHtml(p.productName)}">Consumir</button>
                <button class="iconbtn" data-act="view-batches" data-name="${escapeHtml(p.productName)}">Ver</button>
              </div>
            </div>
          `;
        }

        html += `
          <div class="shelf">
            <div class="shelf-head">
              <h3 class="shelf-title">${escapeHtml(category)}</h3>
              <div class="shelf-meta">${items.length} productos</div>
            </div>
            <div class="products">${productsHtml}</div>
          </div>
        `;
      }

      el.shelves.innerHTML = html;

      el.shelves.querySelectorAll("[data-act]").forEach((btn) => {
        btn.addEventListener("click", async () => {
          const act = btn.dataset.act;
          const name = btn.dataset.name;

          if (act === "fill-batch") {
            el.batchProductName.value = name;
            el.batchAmount.focus();
            setMsg(el.batchMsg, `Seleccionado "${name}"`, "ok");
            return;
          }

          if (act === "fill-consume") {
            el.consumeProductName.value = name;
            el.consumeAmount.focus();
            setMsg(el.consumeMsg, `Seleccionado "${name}"`, "ok");
            return;
          }

          if (act === "view-batches") {
            await openBatchesModal(name);
          }
        });
      });
    }

    async function refreshAll() {
      setMsg(el.lowMsg, "");
      setMsg(el.productMsg, "");
      setMsg(el.batchMsg, "");
      setMsg(el.consumeMsg, "");

      el.shelves.innerHTML = `<p class="empty">Cargando…</p>`;

      try {
        await loadLowStock();
        const summary = await loadSummary();
        renderShelves(summary);
        el.lastUpdated.textContent = `actualizado ${nowTag()}`;
      } catch (e) {
        el.shelves.innerHTML = `<p class="empty">Error al cargar: ${escapeHtml(e.message)}</p>`;
      }
    }

    // ---- Modal (batches) ----
    function showModal() {
      el.modalBackdrop.classList.remove("hidden");
      el.batchesModal.classList.remove("hidden");
    }

    function hideModal() {
      el.modalBackdrop.classList.add("hidden");
      el.batchesModal.classList.add("hidden");
      el.modalBody.innerHTML = "";
    }

    el.modalCloseBtn.addEventListener("click", hideModal);
    el.modalBackdrop.addEventListener("click", hideModal);
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && !el.batchesModal.classList.contains("hidden")) hideModal();
    });

    function sortBatchesByExpiry(batches) {
      return [...batches].sort((a, b) => String(a.expiryDate).localeCompare(String(b.expiryDate)));
    }

    async function openBatchesModal(productName) {
      el.modalTitle.textContent = `Lotes · ${productName}`;
      el.modalSubtitle.textContent = "Ordenados por caducidad (próximos primero)";
      el.modalBody.innerHTML = `<p class="empty">Cargando…</p>`;
      showModal();

      const { ok, res, body } = await request(API.batches(productName));
      if (!ok || !Array.isArray(body)) {
        el.modalBody.innerHTML = `<p class="empty">Error: ${escapeHtml(errorText({ res, body }))}</p>`;
        return;
      }

      const batches = sortBatchesByExpiry(body);
      if (!batches.length) {
        el.modalBody.innerHTML = `<p class="empty">No hay lotes para este producto aún.</p>`;
        return;
      }

      el.modalBody.innerHTML = batches.map((b) => {
        return `
          <div class="batch-item">
            <div class="left">
              <strong>${escapeHtml(b.productName)}</strong>
              <div class="date">Caducidad: ${escapeHtml(b.expiryDate)}</div>
            </div>
            <div class="right">
              <span class="qty">${escapeHtml(b.quantity)}</span>
            </div>
          </div>
        `;
      }).join("");
    }

    // ---- Forms ----
    el.refreshBtn.addEventListener("click", refreshAll);

    el.addProductForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setMsg(el.productMsg, "Creando…");

      const name = el.productName.value.trim();
      const category = el.productCategory.value;

      if (!name) {
        setMsg(el.productMsg, "El nombre es obligatorio", "err");
        return;
      }

      const payload = { name, category };

      const { ok, res, body } = await request(API.products, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!ok) {
        setMsg(el.productMsg, errorText({ res, body }), "err");
        return;
      }

      setMsg(el.productMsg, "Creado ✅", "ok");
      el.productName.value = "";
      el.batchProductName.value = name;
      await refreshAll();
    });

    el.addBatchForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setMsg(el.batchMsg, "Añadiendo…");

      const productName = el.batchProductName.value.trim();
      const amount = toInt(el.batchAmount.value);
      const expiry = el.batchExpiry.value;

      console.log("DEBUG - Product:", productName);
      console.log("DEBUG - Amount:", amount);
      console.log("DEBUG - Expiry:", expiry);

      if (!productName) {
        setMsg(el.batchMsg, "El nombre del producto es obligatorio", "err");
        return;
      }
      if (!expiry) {
        setMsg(el.batchMsg, "La fecha de caducidad es obligatoria", "err");
        return;
      }
      if (!Number.isFinite(amount) || amount <= 0) {
        setMsg(el.batchMsg, "La cantidad debe ser mayor que cero", "err");
        return;
      }

      const payload = { amount, expiryDate: expiry };

      console.log("DEBUG - Payload:", JSON.stringify(payload));

      const { ok, res, body } = await request(API.batches(productName), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      console.log("DEBUG - Response OK:", ok);
      console.log("DEBUG - Response Body:", body);

      if (!ok) {
        setMsg(el.batchMsg, errorText({ res, body }), "err");
        return;
      }

      setMsg(el.batchMsg, "Añadido ✅", "ok");
      await refreshAll();
    });

    el.consumeForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setMsg(el.consumeMsg, "Consumiendo…");

      const productName = el.consumeProductName.value.trim();
      const amount = toInt(el.consumeAmount.value);

      if (!productName) {
        setMsg(el.consumeMsg, "El nombre del producto es obligatorio", "err");
        return;
      }
      if (!Number.isFinite(amount) || amount <= 0) {
        setMsg(el.consumeMsg, "La cantidad debe ser mayor que cero", "err");
        return;
      }

      const payload = { amount };

      const { ok, res, body } = await request(API.consume(productName), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!ok) {
        setMsg(el.consumeMsg, errorText({ res, body }), "err");
        return;
      }

      setMsg(el.consumeMsg, "Consumido ✅", "ok");
      await refreshAll();
    });

    el.lowStockForm.addEventListener("submit", async (e) => {
      e.preventDefault();
      setMsg(el.lowMsg, "");

      const threshold = toInt(el.lowThreshold.value);
      const category = el.lowCategory.value || null;

      if (!Number.isFinite(threshold) || threshold < 0) {
        setMsg(el.lowMsg, "El umbral debe ser un número no negativo", "err");
        return;
      }

      lowFilter = { threshold, category };
      setLowAppliedTag();

      try {
        await loadLowStock();
        const summary = await loadSummary();
        renderShelves(summary);
        setMsg(el.lowMsg, "Aplicado ✅", "ok");
      } catch (e2) {
        setMsg(el.lowMsg, e2.message, "err");
      }
    });

    el.clearLowBtn.addEventListener("click", async () => {
      lowFilter = { threshold: null, category: null };
      setLowAppliedTag();
      setMsg(el.lowMsg, "Limpiado", "ok");
      await refreshAll();
    });

    // init
    (function init() {
      setLowAppliedTag();
      el.lastUpdated.textContent = "actualizado —";
      refreshAll();
    })();