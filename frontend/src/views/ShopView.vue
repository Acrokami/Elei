<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import shopService from '../service/shop.service';
import type { ShopItem } from '../service/shop.service';

const router = useRouter();

const balance = ref<number>(0);
const items = ref<ShopItem[]>([]);
const isProcessing = ref(false);

const statusMessage = ref('');
const statusType = ref<'success' | 'error' | ''>('');

const returnToHub = () => router.push('/');

const showStatus = (message: string, type: 'success' | 'error') => {
  statusMessage.value = message;
  statusType.value = type;
  setTimeout(() => statusMessage.value = '', 5000);
};

const loadMarketData = async () => {
  try {
    balance.value = await shopService.getWalletBalance();
  } catch (e) {
    console.error('[ECONOMY] Failed to retrieve wallet balance.', e);
  }

  try {
    items.value = await shopService.getAvailableItems();
  } catch (e) {
    console.error('[ECONOMY] Failed to load market inventory.', e);
  }
};

const handlePurchase = async (item: ShopItem) => {
  if (balance.value < item.price) {
    showStatus('Insufficient credits for this transaction.', 'error');
    return;
  }

  isProcessing.value = true;
  try {
    await shopService.purchaseItem(item.id);
    showStatus(`Asset [${item.title}] successfully acquired!`, 'success');
    await loadMarketData();
  } catch (e: any) {
    showStatus(e.response?.data || 'Transaction failed.', 'error');
  } finally {
    isProcessing.value = false;
  }
};

onMounted(() => {
  loadMarketData();
});
</script>

<template>
  <div class="page-wrapper">
    <div class="ambient-glow glow-1"></div>
    <div class="ambient-glow glow-2"></div>

    <div class="topbar">
      <span class="logo">Elei<span class="logo-dot">.</span></span>
      <div class="topbar-actions">
        <div class="wallet-badge glass-panel">
          <span class="wallet-icon">💎</span>
          <span class="wallet-amount">{{ balance }}</span>
          <span class="wallet-currency">CRD</span>
        </div>

        <button class="back-btn glass-panel" @click="returnToHub">
          <span class="back-icon">←</span>
          <span>Return</span>
        </button>
      </div>
    </div>

    <div class="content">
      <main class="main-workspace">
        <div class="section-header">
          <div class="header-accent"></div>
          <h2>System Market</h2>
        </div>

        <transition name="slide-fade">
          <div v-if="statusMessage" class="status-banner" :class="`status-${statusType}`">
            <span>{{ statusMessage }}</span>
          </div>
        </transition>

        <p class="market-desc">Exchange your earned experience for real-world rewards and system privileges.</p>

        <div class="market-grid">
          <div v-for="item in items" :key="item.id" class="item-card glass-panel">
            <div class="item-header">
              <h3 class="item-title">{{ item.title }}</h3>
              <div class="item-stock" :class="{ 'low-stock': item.stock < 5 }">
                Stock: {{ item.stock }}
              </div>
            </div>

            <p class="item-desc">{{ item.description }}</p>

            <div class="item-footer">
              <div class="item-price" :class="{ 'cant-afford': balance < item.price }">
                <span class="price-icon">💎</span> {{ item.price }}
              </div>
              <button
                class="purchase-btn"
                :disabled="isProcessing || balance < item.price || item.stock <= 0"
                @click="handlePurchase(item)"
              >
                {{ item.stock <= 0 ? 'Out of Stock' : 'Acquire' }}
              </button>
            </div>
          </div>
        </div>

      </main>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap");

.page-wrapper { min-height: 100vh; background-color: #0b1120; font-family: "Inter", sans-serif; display: flex; flex-direction: column; position: relative; overflow: hidden; color: #f8fafc; }
.ambient-glow { position: absolute; border-radius: 50%; filter: blur(120px); z-index: 0; opacity: 0.3; pointer-events: none; }
.glow-1 { width: 400px; height: 400px; background: rgba(234, 179, 8, 0.2); top: -100px; left: -100px; }
.glow-2 { width: 500px; height: 500px; background: var(--primary-glow); bottom: -200px; right: -100px; }

.topbar, .content { position: relative; z-index: 1; }
.topbar { display: flex; justify-content: space-between; align-items: center; padding: 20px 40px; background: rgba(11, 17, 32, 0.5); backdrop-filter: blur(12px); border-bottom: 1px solid rgba(255, 255, 255, 0.05); }
.logo { font-size: 22px; font-weight: 700; letter-spacing: 0.05em; color: #fff; }
.logo-dot { color: var(--primary-accent); }

.topbar-actions { display: flex; align-items: center; gap: 16px; }

.glass-panel { background: rgba(30, 41, 59, 0.4); backdrop-filter: blur(16px); border: 1px solid rgba(255, 255, 255, 0.08); border-radius: 16px; }

.wallet-badge { display: flex; align-items: center; gap: 8px; padding: 8px 16px; border-color: rgba(234, 179, 8, 0.3); background: rgba(234, 179, 8, 0.05); }
.wallet-icon { font-size: 14px; }
.wallet-amount { font-weight: 700; color: #fde047; font-size: 16px; letter-spacing: 0.05em; }
.wallet-currency { font-size: 11px; color: #a1a1aa; font-weight: 600; }

.back-btn { display: flex; align-items: center; gap: 8px; padding: 8px 16px; color: #cbd5e1; border-radius: 8px; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.3s; background: transparent; }
.back-btn:hover { color: #fff; background: rgba(255, 255, 255, 0.05); transform: translateX(-2px); }

.content { display: flex; justify-content: center; padding: 40px; flex: 1; }
.main-workspace { width: 100%; max-width: 1000px; }

.section-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.header-accent { width: 6px; height: 24px; background: #eab308; border-radius: 4px; box-shadow: 0 0 10px rgba(234, 179, 8, 0.4); }
.section-header h2 { font-size: 20px; font-weight: 700; margin: 0; }
.market-desc { color: #94a3b8; font-size: 14px; margin-bottom: 32px; }

.market-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 24px; }

.item-card { padding: 20px; display: flex; flex-direction: column; transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1); }
.item-card:hover { transform: translateY(-4px); border-color: rgba(255, 255, 255, 0.15); box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2); }

.item-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.item-title { font-size: 16px; font-weight: 700; color: #f8fafc; margin: 0; }
.item-stock { font-size: 11px; font-weight: 600; color: #94a3b8; background: rgba(0,0,0,0.2); padding: 4px 8px; border-radius: 4px; }
.low-stock { color: #ef4444; background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.2); }

.item-desc { font-size: 13px; color: #cbd5e1; line-height: 1.5; flex: 1; margin-bottom: 20px; }

.item-footer { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid rgba(255, 255, 255, 0.05); padding-top: 16px; }
.item-price { display: flex; align-items: center; gap: 6px; font-weight: 700; color: #fde047; font-size: 18px; }
.cant-afford { color: #ef4444; }

.purchase-btn { background: #eab308; color: #000; font-weight: 700; padding: 8px 16px; border: none; border-radius: 6px; cursor: pointer; transition: all 0.2s; font-size: 13px; }
.purchase-btn:hover:not(:disabled) { background: #fef08a; box-shadow: 0 0 15px rgba(234, 179, 8, 0.4); }
.purchase-btn:disabled { opacity: 0.5; cursor: not-allowed; filter: grayscale(100%); }

.status-banner { display: flex; align-items: center; gap: 12px; padding: 14px 20px; border-radius: 12px; margin-bottom: 24px; font-size: 14px; font-weight: 500; }
.status-success { background: rgba(16, 185, 129, 0.1); border: 1px solid var(--primary-accent); color: var(--primary-accent); }
.status-error { background: rgba(239, 68, 68, 0.1); border: 1px solid #ef4444; color: #ef4444; }
.slide-fade-enter-active { transition: all 0.4s ease-out; }
.slide-fade-leave-active { transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1); }
.slide-fade-enter-from, .slide-fade-leave-to { transform: translateY(-10px); opacity: 0; }
</style>
