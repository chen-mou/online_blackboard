export function int32ToBytes(n: number): string {
  let res = ''
  res += String.fromCharCode((n >>> 24) & 0xFF)
  res += String.fromCharCode((n >>> 16) & 0xFF)
  res += String.fromCharCode((n >>> 8) & 0xFF)
  res += String.fromCharCode(n & 0xFF)
  return res
}

export function bytesToInt32(b: string): number {
  return (((((b.charCodeAt(0) << 8) + b.charCodeAt(1)) << 8) + b.charCodeAt(2)) << 8) + b.charCodeAt(3)
}
