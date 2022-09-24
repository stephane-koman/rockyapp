import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface IPermission extends IDefault {
  name: string;
  description: string;
}

export interface IPermissionCriteriaSearch extends IDefaultCriteriaSearch {
  name?: string;
  description?: string;
}
