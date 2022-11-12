import { IFrame, Stomp } from "@stomp/stompjs";
import Socket from 'sockjs-client'

export function useWs(
  roomId: number,
  isAnonymous: number,
  channels: Array<{ id: string, channel: string, callback: (data: IFrame) => void }>,
  onDisconnect: (frame: IFrame) => void
) {
  const client = Stomp.over(() => new Socket('http://localhost:18888/connect'))
  client.onStompError = onDisconnect

  const headers = {
    Authorization: localStorage.getItem('token'),
    'Room-Id': roomId,
    'Is-Anonymous': isAnonymous,
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
    send(id: string, data: unknown) {
      client.send((channels.find(e => e.id === id) as any).channel, {}, JSON.stringify(data))
    },
    sendRaw(destination: string, headers: { [p: string]: any }, body: string) {
      client.send(destination, headers, body)
    },
    async close() {
      await client.deactivate()
      this.active = false
    },
    active: true,
  }
}
