import jwt_decode from "jwt-decode";
import { JWT_TOKEN } from "../constants/global.constant";
import { IJwtToken } from "../interfaces/global.interface";

export const userIsLoggedIn = (): boolean => {
  const jwtTokenParse: IJwtToken = extractTokenParse();
  
  if(jwtTokenParse){
    const jwtTokenDecoded: any = jwt_decode(jwtTokenParse?.access_token);
    return jwtTokenDecoded?.exp < (new Date()).getTime();
  }
  return false;
};

export const getUserPermissions = () => {
  const jwtTokenParse: IJwtToken = extractTokenParse();
  if (jwtTokenParse){
    const jwtTokenDecoded: any = jwt_decode(jwtTokenParse?.access_token);
    return jwtTokenDecoded?.permissions;
  }

  return  [];
};

export const extractTokenParse = (): IJwtToken => {
  const jwtToken: any = localStorage.getItem(JWT_TOKEN);
  return jwtToken && JSON.parse(jwtToken) || null;
};
