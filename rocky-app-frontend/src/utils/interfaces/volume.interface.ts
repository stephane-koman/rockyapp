import { EMesure } from "../enums/mesure.enum";
import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface IVolume extends IDefault {
  quantity: string;
  mesure: EMesure;
  description: string;
}

export interface IVolumeCriteriaSearch extends IDefaultCriteriaSearch {
  quantity?: number;
  mesure?: EMesure;
}