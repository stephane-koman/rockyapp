import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  IPermission,
  IPermissionCriteriaSearch,
} from "../utils/interfaces/permission.interface";

class PermissionService {
  private endPointPath: string = "/permission";

  findById = (userId: number): Promise<AxiosResponse<IPermission>> => {
    return axiosApiInstance.get<IPermission>(`${this.endPointPath}/${userId}`);
  };

  create = (permission: IPermission): Promise<AxiosResponse<IPermission>> => {
    return axiosApiInstance.post<IPermission>(this.endPointPath, permission);
  };

  update = (
    id: number,
    permission: IPermission
  ): Promise<AxiosResponse<IPermission>> => {
    return axiosApiInstance.put<IPermission>(
      `${this.endPointPath}/${id}`,
      permission
    );
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`/permission/${id}`);
  };

  search = (
    data?: IPermissionCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<IPermission>>> => {
    return axiosApiInstance.post<IApiResponse<IPermission>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}

export const permissionService = new PermissionService();
