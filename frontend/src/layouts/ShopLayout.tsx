import { Navigate, Outlet, useParams } from "react-router-dom";
import { useGetShopById } from "@/api/generated/shops/shops.ts";


export default function ShopLayout() {

    const { shopId } = useParams();

    const { data: shopData, isLoading } = useGetShopById( shopId || "", {
        query: {
            enabled: !!shopId,
        }
    } );

    if ( isLoading ) return <p>Loading</p>

    if ( !shopData ) return <Navigate to={ "/" }/>;


    return (
        <Outlet/>
    )
}
