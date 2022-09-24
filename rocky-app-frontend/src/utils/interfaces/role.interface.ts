import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface ISimpleRole extends IDefault {
  name: string;
  description: string;
}

export interface IRole extends ISimpleRole {
  permissionList: string[];
}

export interface IRoleCriteriaSearch extends IDefaultCriteriaSearch {
  name?: string;
  description?: string;
}