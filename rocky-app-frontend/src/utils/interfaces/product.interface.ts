import { IDocument } from "./document.interface";
import { IDefault, IDefaultCriteriaSearch } from "./global.interface";
import { IProductType } from "./product-type.interface";
import { IVolume } from "./volume.interface";

interface IProductDefault extends IDefault {
  code: string;
  name: string;
  description: string;
  price: number;
}

export interface ISimpleProduct extends IProductDefault {
  productType: IProductType;
  volume: IVolume;
}

export interface IProduct extends ISimpleProduct {
  documentList: IDocument[];
}

export interface IProductCrea extends IProductDefault {
  documentList: IDocument[];
  productTypeId: number;
  volumeId: number;
}

export interface IProductCriteriaSearch extends IDefaultCriteriaSearch {
  code?: string;
  name?: string;
  price?: string;
  productTypeList?: string[];
  volumeList?: string[];
}