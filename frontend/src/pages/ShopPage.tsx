import { Navigate, useParams } from "react-router-dom";
import ShopDetail from "@/components/shop/ShopDetail.tsx";

export default function ShopPage() {
    const { shopId } = useParams();

    if ( !shopId ) {
        return <Navigate to="/shop"/>;
    }

    return <div className="p-8 w-full">
        <ShopDetail shopId={ shopId }/>
    </div>;
}
