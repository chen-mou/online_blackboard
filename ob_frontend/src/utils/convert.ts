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
  line: 'Line',
  freeLine: 'FreeLine',
  FreeLine: 'freeLine',
  ellipse: 'Ellipse',
  Ellipse: 'ellipse',
}

export function shapeToWSShape(shape: any, sheetId: number, roomId: number): any {
  console.log(shape)
  const data = {
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
  if (shape.type === 'freeLine') {
    // @ts-ignore
    data.shape['data'] = shape.data
  }
  return data
}

export function wsShapeToShape(wsShape: any): any {
  const data = {
    "id": wsShape.id,
    "type": shapeTypeMap[wsShape.type],
    "BeforePosition": [
      wsShape.start.x,
      wsShape.start.y,
    ],
    "AfterPosition": [
      wsShape.end.x,
      wsShape.end.y,
    ],
    "pen": {
      "icon": "",
      "linewidth": wsShape.pen.lineWidth,
      "strokeStyle": wsShape.pen.strokeStyle,
      "fillStyle": wsShape.pen.fillStyle,
    }
  }
  if (wsShape.type === 'FreeLine') {
    // @ts-ignore
    data['data'] = wsShape.data
  }
  return data
}
