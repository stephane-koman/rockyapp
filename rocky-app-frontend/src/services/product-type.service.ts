import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  IProductType,
  IProductTypeCriteriaSearch,
} from "../utils/interfaces/product-type.interface";

class ProductTypeService {
  private endPointPath: string = "/product-type";

  findById = (productTypeId: number): Promise<AxiosResponse<IProductType>> => {
    return axiosApiInstance.get<IProductType>(
      `${this.endPointPath}/${productTypeId}`
    );
  };

  create = (productType: IProductType): Promise<AxiosResponse<IProductType>> => {
    return axiosApiInstance.post<IProductType>(this.endPointPath, productType);
  };

  update = (
    id: number,
    productType: IProductType
  ): Promise<AxiosResponse<IProductType>> => {
    return axiosApiInstance.put<IProductType>(
      `${this.endPointPath}/${id}`,
      productType
    );
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  search = (
    data: IProductTypeCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<IProductType>>> => {
    return axiosApiInstance.post<IApiResponse<IProductType>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}
export const productTypeService = new ProductTypeService();
