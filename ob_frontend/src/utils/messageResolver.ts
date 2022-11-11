export const userInfoMessageResolver: { [k: string]: (store: any, data: any) => void } = {
  'room_info': (store: any, data: any) => {
    store.sheetId = data.sheet == 0 ? data.data.nowSheet : data.sheet
  },
  'voice_url': (store: any, data: any) => {
    return
  }
}

