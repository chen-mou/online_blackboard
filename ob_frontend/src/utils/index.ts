/**
 * 深拷贝
 * @param Obj 
 */
export function deepCopy<T>(tSource: T, tTarget?: Record<string, any> | T): T {
    if (IsArray(tSource)) {
        tTarget = tTarget || [];
    } else {
        tTarget = tTarget || {};
    }
    for (const key in tSource) {
        if (Object.prototype.hasOwnProperty.call(tSource, key)) {
            if (typeof tSource[key] === "object" && typeof tSource[key] !== null) {
                tTarget[key] = IsArray(tSource[key]) ? [] : {};
                deepCopy(tSource[key], tTarget[key]);
            } else {
                tTarget[key] = tSource[key];
            }
        }
    }
    return tTarget as T;
}
/**
 * 
 * @param key 
 * @param object 
 * @returns 
 */
export function isValidKey(
    key: string | number | symbol,
    object: object
): key is keyof typeof object {
    return key in object;
}
/**
 *  
 * @param Obj 
 * @returns 
 */

export function IsObject(Obj:any){
    return Object.prototype.toString.call(Obj)!=="[object Object]"
}
export function IsArray(obj: any) {
    return obj && typeof obj == "object" && obj instanceof Array;
}