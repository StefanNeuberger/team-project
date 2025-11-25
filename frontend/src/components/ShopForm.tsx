import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useQueryClient } from "@tanstack/react-query";

import { Button } from "./ui/button";
import { Input } from "./ui/input";
import {
  useCreateShop,
  getGetAllShopsQueryKey,
} from "../api/generated/shops/shops";

const shopFormSchema = z.object({
  name: z
    .string()
    .min(1, "Shop name is required")
    .min(2, "Shop name must be at least 2 characters")
    .max(100, "Shop name must be less than 100 characters")
    .trim(),
});

type ShopFormData = z.infer<typeof shopFormSchema>;

export function ShopForm() {
  const queryClient = useQueryClient();
  const {
    register,
    handleSubmit,
    setError,
    reset,
    formState: { errors },
  } = useForm<ShopFormData>({
    resolver: zodResolver(shopFormSchema),
  });

  // POST create shop
  const createShop = useCreateShop();

  const handleCreateShop = (data: ShopFormData) => {
    createShop.mutate(
      { data: { name: data.name } },
      {
        onSuccess: () => {
          // Reset form and invalidate query
          reset();
          queryClient.invalidateQueries({ queryKey: getGetAllShopsQueryKey() });
        },
        onError: (error: any) => {
          if (error?.response?.status === 409) {
            console.error("Failed to create shop:", error);
            setError("name", {
              type: "manual",
              message: error.response.data.message,
            });
          } else {
            // For other errors, show generic error
            console.error("Failed to create shop:", error);
          }
        },
      }
    );
  };

  return (
    <div className="space-y-2">
      <form onSubmit={handleSubmit(handleCreateShop)} className="space-y-2">
        <div className="flex gap-2">
          <div className="flex-1">
            <Input
              {...register("name")}
              placeholder="Enter shop name"
              aria-invalid={errors.name ? "true" : "false"}
            />
            {errors.name && (
              <p className="text-sm text-destructive mt-1">
                {errors.name.message}
              </p>
            )}
          </div>
          <Button type="submit" disabled={createShop.isPending}>
            {createShop.isPending ? "Creating..." : "Add Shop"}
          </Button>
        </div>
      </form>

      {createShop.isError && !errors.name && (
        <div className="p-3 bg-destructive text-destructive-foreground">
          Failed to create shop. Please try again.
        </div>
      )}
    </div>
  );
}

export { shopFormSchema };
export type { ShopFormData };
