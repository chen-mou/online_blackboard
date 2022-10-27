import { defineStore } from "pinia";

export const useUserStore = defineStore('user', {
    state: () => ({
      login: false,
    }),
    actions: {},
    getters: {},
  }
)