import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  IProduct,
  IProductCrea,
  IProductCriteriaSearch,
} from "../utils/interfaces/product.interface";

class ProductService {
  private endPointPath: string = "/product";

  findById = (productId: number): Promise<AxiosResponse<IProduct>> => {
    return axiosApiInstance.get<IProduct>(`${this.endPointPath}/${productId}`);
  };

  create = (product: IProductCrea): Promise<AxiosResponse<void>> => {
    return axiosApiInstance.post<void>(this.endPointPath, product);
  };

  update = (
    id: number,
    product: IProductCrea
  ): Promise<AxiosResponse<void>> => {
    return axiosApiInstance.put<void>(`${this.endPointPath}/${id}`, product);
  };

  delete = (id: number): Promise<AxiosResponse<void>> => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  search = (
    data: IProductCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<IProduct>>> => {
    return axiosApiInstance.post<IApiResponse<IProduct>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}
export const productService = new ProductService();
