export const userInfoMessageResolver = {
  'room_info': (store: any, data: any) => {
    store.sheetId = data.sheet == 0 ? data.data.nowSheet : data.sheet
  },
}

