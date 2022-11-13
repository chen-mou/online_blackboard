export function throttleCall(func: (e: any) => void) {
  let previous = 0;
  return (e: any) => {
    console.log(e)
    const now = +new Date();
    if (now - previous > 5) {
      previous = now
      func(e)
    }
  }
}