import { IFrame, Stomp } from "@stomp/stompjs";
import Socket from 'sockjs-client'

export function useWs(roomId: string, isAnonymous: number, channels: Array<{ channel: string, callback: (data: IFrame) => void }>) {
  const client = Stomp.over(new Socket('http://47.112.184.57:18888/connect'))
  const headers = {
    Authorization: localStorage.getItem('token'),
    'Room-Id': roomId,
    isAnonymous,
  }
  client.connect(
    headers,
    () => {
      for (const { channel, callback } of channels) {
        client.subscribe(channel, callback)
      }
      client.send('/app/room_info', {}, JSON.stringify({ roomId }))
    },
  )
  return {
    send(channel: string, data: unknown) {
      client.send(channel, {}, JSON.stringify(data))
    },
    close() {
      client.deactivate()
    },
  }
}
