import { useGetAllShops } from "../api/generated/shops/shops";
import { ShopForm } from "./ShopForm";

export function ShopList() {
  // GET all shops
  const { data: shopsResponse, isLoading, error } = useGetAllShops();

  if (isLoading) {
    return <div className="p-4">Loading shops...</div>;
  }

  if (error) {
    return (
      <div className="p-4 text-destructive">
        Error loading shops: {String(error)}
      </div>
    );
  }

  const shops = shopsResponse?.data ?? [];

  return (
    <div className="p-4 flex flex-col items-center justify-center gap-4">
      {/* Create Shop Form */}
      <ShopForm />

      {/* Shop List */}
      <div className="space-y-2 w-full">
        <h2 className="text-xl font-semibold mb-2">Shops ({shops.length})</h2>
        {shops.length === 0 ? (
          <p className="text-muted-foreground">
            No shops yet. Create one above!
          </p>
        ) : (
          shops.map((shop) => (
            <div
              key={shop.id}
              className="p-2 border border-border hover:bg-accent"
            >
              <div className="font-medium">{shop.name}</div>
              <div className="text-xs text-muted-foreground">ID: {shop.id}</div>
              {shop.createdDate && (
                <div className="text-xs text-muted-foreground">
                  Created: {new Date(shop.createdDate).toLocaleString()}
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
}
