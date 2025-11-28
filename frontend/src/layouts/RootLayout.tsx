import { useGetAllShops } from "@/api/generated/shops/shops.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import { ShopList } from "@/components/ShopList.tsx";
import { Navigate } from "react-router-dom";

export default function RootLayout() {
  const { data: shopResponse, isLoading, error } = useGetAllShops();

  if (error) {
    throw error;
  }

  if (isLoading || !shopResponse) {
    return <Loading />;
  }

  if (shopResponse.data.length == 0) {
    return <ShopList></ShopList>;
  }

  return <Navigate to={`/shop`} />;
}
