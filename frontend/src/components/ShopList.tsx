import { useGetAllShops } from "../api/generated/shops/shops";
import { ShopForm } from "./ShopForm";
import { NavLink } from "react-router-dom";
import Loading from "@/components/custom-ui/Loading.tsx";

export function ShopList() {
    // GET all shops
    const { data: shopsResponse, isLoading, error } = useGetAllShops();

    if ( error ) {
        throw error;
    }
    
    const shops = shopsResponse?.data ?? [];

    return (
        <div className="p-4 flex flex-col flex-1 items-center justify-start gap-4">

            { isLoading || !shopsResponse ?
                <Loading/>
                :
                <>
                    <h1 className={ "text-accent-foreground font-bold my-8 text-2xl" }>Choose a Shop or create
                        one</h1>
                    {/* Create Shop Form */ }
                    <ShopForm/>

                    {/* Shop List */ }
                    <div className="space-y-2">
                        <h2 className="text-xl font-semibold mb-2">Shops ({ shops.length })</h2>
                        { shops.length === 0 ? (
                            <p className="text-muted-foreground">
                                No shops yet. Create one above!
                            </p>
                        ) : (
                            shops.map( ( shop ) => (
                                <div
                                    key={ shop.id }
                                    className="p-2 border border-border hover:bg-accent"
                                >
                                    <div className="font-medium">{ shop.name }</div>
                                    <div className="text-xs text-muted-foreground">ID: { shop.id }</div>
                                    { shop.createdDate && (
                                        <div className="text-xs text-muted-foreground">
                                            Created: { new Date( shop.createdDate ).toLocaleString() }
                                        </div>
                                    ) }
                                    <NavLink to={ `/shop/${ shop.id }` }>Go to Shop</NavLink>
                                </div>
                            ) )
                        ) }
                    </div>
                </>
            }
        </div>
    );
}
