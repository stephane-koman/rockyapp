import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface ISimpleUser extends IDefault {
  name: string;
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
  name?: string;
  username?: string;
  email?: string;
  roleList?: string[];
}