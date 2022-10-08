import { EMesure } from "../enums/mesure.enum";
import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface IProductType extends IDefault {
  name: string;
  description: string;
}

export interface IProductTypeCriteriaSearch extends IDefaultCriteriaSearch {
  name?: string;
}