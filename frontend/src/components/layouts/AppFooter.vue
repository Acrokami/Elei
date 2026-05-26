<script setup lang="ts">
const currentYear = new Date().getFullYear();
</script>

<template>
  <footer class="app-footer">
    <div class="footer-content">
      <div class="brand">
        <span class="logo">Elei<span class="logo-dot">.</span></span>
        <span class="version">v1.0.0</span>
      </div>
      <div class="copyright">
        &copy; {{ currentYear }} Crafted by <span class="author">Acr0</span>
      </div>
    </div>
  </footer>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

.app-footer {
  width: 100%;
  background: rgba(11, 17, 32, 0.6);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  padding: 24px 0;
  margin-top: auto;
  font-family: 'Inter', sans-serif;
  position: relative;
  z-index: 10;
}

.footer-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 0.05em;
  background: linear-gradient(to right, #64748b, #94a3b8);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  transition: all 0.3s ease;
  cursor: default;
}

.logo:hover {
  background: linear-gradient(to right, #f8fafc, #cbd5e1);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.logo-dot {
  color: #3b82f6;
  -webkit-text-fill-color: #3b82f6;
}

.version {
  color: #64748b;
  font-size: 11px;
  font-family: monospace;
  background: rgba(255, 255, 255, 0.05);
  padding: 4px 8px;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.05);
  letter-spacing: 0.05em;
}

.copyright {
  color: #475569;
  font-size: 13px;
  font-weight: 500;
}

.author {
  color: #94a3b8;
  transition: color 0.3s ease;
  cursor: pointer;
}

.author:hover {
  color: #38bdf8;
}


@media (max-width: 600px) {
  .footer-content {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
}
</style>
