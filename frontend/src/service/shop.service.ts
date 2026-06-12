import api from './api';

export interface ShopItem {
  id: number;
  title: string;
  description: string;
  price: number;
  stock: number;
}

const shopService = {

  getWalletBalance: async (): Promise<number> => {
    const response = await api.get('/shop/wallet');
    return response.data;
  },


  getAvailableItems: async (): Promise<ShopItem[]> => {
    const response = await api.get('/shop/items');
    return response.data;
  },

  
  purchaseItem: async (itemId: number): Promise<string> => {
    const response = await api.post(`/shop/purchase/${itemId}`);
    return response.data;
  }
};

export default shopService;
