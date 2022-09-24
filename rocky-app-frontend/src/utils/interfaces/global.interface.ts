import { ItemType } from "antd/lib/menu/hooks/useItems";
import React from "react";
import { EActionType, ETableActionType } from "../enums/global.enum";

export interface IDefault {
  id: number;
  active?: boolean;
}

export interface IStatus {
  active?: boolean;
}

export interface IJwtToken {
  access_token: string;
  refresh_token: string;
}

export interface IPagination {
  page?: number;
  size?: number;
  total?: number;
  sorts?: string[]
}

export interface IApiResponse<T> extends IPagination {
  results: T[];
  totalPages: number;
}

export interface IDefaultCriteriaSearch {
  active?: number;
  text_search?: string;
}

export interface IDeleteModal {
  id: any;
  info: any;
  onConfirm: (data?: any) => void;
  onCancel: (data?: any) => void;
}

export interface ITableActions {
  type?: ETableActionType;
  data: any;
  children?: React.ReactNode;
  items?: ItemType[];
  permission?: {
    read?: string;
    update?: string;
    delete?: string;
  };
  deleteInfo?: any;
  handleAction?: (data: any, type: EActionType) => void;
  handleOtherAction?: (data: any, type?: string) => void;
  onCancelDelete?: (data?: any) => void;
  onConfirmDelete?: (id?: any) => void;
}

export interface ITableHeaderActions {
  search?: boolean;
  refresh?: boolean;
  children?: React.ReactNode;
  onSearch?: (value: string) => void;
  onRefresh?: () => void;
}