import React from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { userIsLoggedIn } from "../../utils/helpers/auth.helper";

const GuardedRoute = () => {
  const location = useLocation();
  const isUserConnected = userIsLoggedIn();

  return isUserConnected ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} />
  );
};

export default React.memo(GuardedRoute);
