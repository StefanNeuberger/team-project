import { Navigate, useParams } from "react-router-dom";
import { useGetShopById } from "@/api/generated/shops/shops";
import Loading from "@/components/custom-ui/Loading";

export default function ShopPage() {
  const { shopId } = useParams();

  if (!shopId) {
    return <Navigate to="/shop" />;
  }

  const {
    data: shopResponse,
    isLoading,
    error,
  } = useGetShopById(shopId, { query: { enabled: !!shopId } });

  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  return <>Shop {shopResponse?.data?.name}</>;
}
