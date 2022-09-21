import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface ISimpleUser extends IDefault {
  username: string;
  email: string;
  roleList: string[];
}

export interface IUser extends ISimpleUser {
  roleList: string[];
  permissionList: string[];
}

export interface IPassword {
  password: string;
  passwordConfirm: string;
}
export interface IUserCrea extends IUser, IPassword {}

export interface IUserCriteriaSearch extends IDefaultCriteriaSearch {
  username?: string;
  email?: string;
  roleList?: string[];
}