export const unionArrays = (x: any[], y: any[]) => {
  let obj: any = {};
  for (let i = x.length - 1; i >= 0; --i) obj[x[i]] = x[i];
  for (let j = y.length - 1; j >= 0; --j) obj[y[j]] = y[j];
  let res: any[] = [];
  for (let k in obj) {
    if (obj.hasOwnProperty(k))
      // <-- optional
      res.push(obj[k]);
  }
  return res;
};
