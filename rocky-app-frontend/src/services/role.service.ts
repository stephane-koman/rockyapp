import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  IRole,
  IRoleCriteriaSearch,
  ISimpleRole,
} from "../utils/interfaces/role.interface";

class RoleService {
  private endPointPath: string = "/role";

  findById = (roleId: number): Promise<AxiosResponse<IRole>> => {
    return axiosApiInstance.get<IRole>(`${this.endPointPath}/${roleId}`);
  };

  create = (role: IRole): Promise<AxiosResponse<IRole>> => {
    return axiosApiInstance.post<IRole>(this.endPointPath, role);
  };

  update = (id: number, role: IRole): Promise<AxiosResponse<IRole>> => {
    return axiosApiInstance.put<IRole>(`${this.endPointPath}/${id}`, role);
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  search = (
    data?: IRoleCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<ISimpleRole>>> => {
    return axiosApiInstance.post<IApiResponse<ISimpleRole>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}

export const roleService = new RoleService();
