import { IDefault, IDefaultCriteriaSearch } from "./global.interface";

export interface ICustomer extends IDefault {
  name: string;
  email: string;
  mobile: string;
  fixe: string;
  address: string;
  description: string;
}

export interface ICustomerCriteriaSearch extends IDefaultCriteriaSearch {
  name?: string;
  email?: string;
  mobile?: string;
  fixe?: string;
  address?: string;
}