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

const shapeTypeMap: { [k: string]: string } = {
  Cube: 'Rect',
  Round: 'circle',
  Line: 'line',
  Rect: 'Cube',
  circle: 'Round',
  line: 'line',
}

export function shapeToWSShape(shape: any, sheetId: number, roomId: number): any {
  return {
    "shape": {
      "type": shapeTypeMap[shape.type],
      "color": ".",
      "start": {
        "x": shape.BeforePosition[0],
        "y": shape.BeforePosition[1],
      },
      "end": {
        "x": shape.AfterPosition[0],
        "y": shape.AfterPosition[1],
      },
      "pen": {
        "lineWidth": shape.pen.linewidth,
        "strokeStyle": shape.pen.strokeStyle,
        "fillStyle": shape.pen.fillStyle,
      }
    },
    "sheetId": sheetId,
    "roomId": roomId,
  }
}

export function wsShapeToShape(wsShape: any): any {
  return {
    "type": shapeTypeMap[wsShape.shape.type],
    "BeforePosition": [
      wsShape.shape.start.x,
      wsShape.shape.start.y,
    ],
    "AfterPosition": [
      wsShape.shape.end.x,
      wsShape.shape.end.y,
    ],
    "pen": {
      "icon": "",
      "linewidth": wsShape.shape.pen.lineWidth,
      "strokeStyle": wsShape.shape.pen.strokeStyle,
      "fillStyle": wsShape.shape.pen.fillStyle,
    }
  }
}
