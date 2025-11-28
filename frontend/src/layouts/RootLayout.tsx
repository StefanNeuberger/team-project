import Footer from "@/components/Footer.tsx";
import { Outlet, useLocation } from "react-router-dom";
import Header from "@/components/Header.tsx";
import { useGetAllShops } from "@/api/generated/shops/shops.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import { ShopList } from "@/components/ShopList.tsx";

export default function RootLayout() {

    const { data: shopResponse, isLoading, error } = useGetAllShops();
    const location = useLocation();
    const isHomePage = location.pathname === '/';

    if ( error ) {
        throw error;
    }

    if ( isLoading || !shopResponse ) {
        return <Loading/>
    }

    return (
        <>
            <Header/>
            <div className={ "flex-1 flex" }>
                { isHomePage && <ShopList/> }
                <Outlet/>
            </div>
            <Footer/>
        </>
    )
}