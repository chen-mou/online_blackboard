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

const shapeTypeMap = {
  Cube: 'Rect',
  Round: 'circle',
  Line: 'line',
  Rect: 'Cube',
  circle: 'Round',
  line: 'line',
}

export function shapeToWSShape(shape: any): any {
  return {
    "shape": {
      "type": (shapeTypeMap as any)[shape.type],
      "color": ".",
      "start": {
        "x": 2,
        "y": 2
      },
      "end": {
        "x": 0,
        "y": 0
      },
      "pen": {
        "lineWidth": 5,
        "strokeStyle": "",
        "fillStyle": ""
      }
    },
    "sheetId": 1000672,
    "roomId": 1000408
  }
}

export function wsShapeToShape(wsShape: any): any {
''
}
