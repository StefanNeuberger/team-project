import { useGetAllShops } from "@/api/generated/shops/shops";
import Loading from "@/components/custom-ui/Loading";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ShopForm } from "@/components/ShopForm";
import { useNavigate } from "react-router-dom";

export const ShopsPage = () => {
  const { data: shopResponse, isLoading, error } = useGetAllShops();
  const navigate = useNavigate();
  if (isLoading) {
    return <Loading />;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <h1 className="text-2xl font-bold">Shops</h1>
        <p className="text-sm text-muted-foreground">
          Select a shop to view its inventory, shipments, and more.
        </p>
      </div>
      {shopResponse?.data?.length && shopResponse?.data?.length > 0 ? (
        <Select
          onValueChange={(value) => {
            navigate(`/shop/${value}`);
          }}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Select a shop" />
          </SelectTrigger>
          <SelectContent>
            {shopResponse?.data.map((shop) => (
              <SelectItem key={shop.id} value={shop.id}>
                {shop.name}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      ) : (
        <div>No shops found</div>
      )}
      <span className="text-sm text-muted-foreground">
        or create a new shop
      </span>
      <ShopForm />
    </div>
  );
};
