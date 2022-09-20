import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface IPermission extends IDefault {
  description: string;
}

export interface IPermissionCriteriaSearch extends IDefaultCriteriaSearch {
  description?: string;
}
