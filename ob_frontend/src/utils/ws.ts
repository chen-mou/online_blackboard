import { io } from "socket.io-client";


export function useWs(eventName: string, userId: string, callback: (data: unknown) => void) {
  const socket = io('/connect', {
    autoConnect: true,
    extraHeaders: { userId },
  })
  socket.on('connect', () => {
    console.log(socket.id)
  })
  socket.on(eventName, callback)
  return {
    emit(data: unknown) {
      socket.emit(eventName, data)
    },
    close() {
      socket.close()
    },
  }
}
