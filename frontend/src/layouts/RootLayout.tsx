import Footer from "@/components/Footer.tsx";
import { Outlet, useLocation } from "react-router-dom";
import Header from "@/components/Header.tsx";
import { ShopList } from "@/components/ShopList.tsx";
import { Toaster } from "sonner";

export default function RootLayout() {

    const location = useLocation();
    const isHomePage = location.pathname === '/';


    return (
        <>
            <Header/>
            <div className={ "flex-1 flex" }>
                { isHomePage && <ShopList/> }
                <Outlet/>
            </div>
            <Toaster/>
            <Footer/>
        </>
    )
}