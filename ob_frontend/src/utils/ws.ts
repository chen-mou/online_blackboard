import { Stomp } from "@stomp/stompjs";

export function useWs(eventName: string, callback: (data: unknown) => void) {
  const client = Stomp.client('/connect')
  const headers = {
    Authorization: localStorage.getItem('token')
  }
  client.connect(headers, callback)
  return {
    emit(data: unknown) {
      client.send(eventName, {}, JSON.stringify(data))
    },
    close() {
      client.disconnect()
    },
  }
}
