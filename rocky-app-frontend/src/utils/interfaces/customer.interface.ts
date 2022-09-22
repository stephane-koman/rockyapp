import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface ICustomer extends IDefault {
  email: string;
  mobile: string;
  fixe: string;
  address: string;
  description: string;
}

export interface ICustomerCriteriaSearch extends IDefaultCriteriaSearch {
  email?: string;
  mobile?: string;
  fixe?: string;
  address?: string;
}