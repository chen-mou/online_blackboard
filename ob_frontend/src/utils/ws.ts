import { io } from "socket.io-client";

const socket = io('/ws', { autoConnect: true })

socket.on('connect', () => {
  console.log(socket.id)
})

export function useWs(eventName: string, callback: (data: unknown) => void) {
  socket.on(eventName, callback)
  return {
    emit(data: unknown) {
      socket.emit(eventName, data)
    },
  }
}
