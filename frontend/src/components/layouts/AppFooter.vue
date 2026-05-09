<script setup lang="ts">
const currentYear = new Date().getFullYear();
</script>

<template>
<footer class="app-footer">
    <div class="footer-content">
      <div class="brand">
        <span class="logo">Elei</span>
        <span class="version">v1.0.0</span>
      </div>
      <div class="copyright">
        &copy; {{ currentYear }} Crafted by Acr0
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
    width: 100%;
    border-top: 1px solid #1e293b;
    background-color: #0f172a;
    padding: 24px 0;
    margin-top: auto;
}

.footer-content {
  max-width: 600px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  color: #64748b;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 0.05em;
  transition: color 0.2s;
}

.logo:hover {
  color: #38bdf8;
}

.version {
  color: #334155;
  font-size: 12px;
  font-family: monospace;
}

.copyright {
  color: #475569;
  font-size: 13px;
}

</style>
