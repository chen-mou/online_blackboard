type ShapeTypes = 'rect' | 'circle' | 'line'

interface IShapeProps {
  id: string
  type: ShapeTypes
  style: {
    width: number
    height: number
    left: number
    top: number
  }
  isEditable: boolean
}
