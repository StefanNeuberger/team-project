import { createBrowserRouter } from "react-router-dom";
import RootLayout from "@/layouts/RootLayout.tsx";
import PageNotFound from "@/pages/PageNotFound.tsx";
import ErrorPage from "@/pages/Error.tsx";
import ShopPage from "@/pages/ShopPage.tsx";
import WarehousesPage from "@/pages/WarehousesPage.tsx";
import ItemPage from "@/pages/ItemsPage.tsx";
import InventoryPage from "@/pages/InventoryPage.tsx";
import ItemDetailPage from "@/pages/ItemDetailPage.tsx";
import WarehouseDetailPage from "@/pages/WarehouseDetailPage.tsx";
import InventoryDetailPage from "@/pages/InventoryDetailPage.tsx";
import ShipmentsPage from "@/pages/ShipmentsPage.tsx";
import ShipmentDetailPage from "@/pages/ShipmentDetailPage.tsx";
import ShopLayout from "@/layouts/ShopLayout.tsx";
import WarehouseEditPage from "@/pages/WarehouseEditPage.tsx";
import WarehouseDeletePage from "@/pages/WarehouseDeletePage.tsx";
import WarehouseCreatePage from "@/pages/WarehouseCreatePage.tsx";


export const router = createBrowserRouter( [
        {
            path: "/",
            element: <RootLayout/>,
            errorElement: <ErrorPage/>,
            children: [
                {
                    path: "shop",
                    element: <ShopLayout/>,
                    children: [
                        {
                            path: ":shopId",
                            element: <ShopPage/>,
                        },
                        {
                            path: ":shopId/items",
                            element: <ItemPage/>
                        },
                        {
                            path: ":shopId/items/:id",
                            element: <ItemDetailPage/>
                        },
                        {
                            path: ":shopId/warehouses",
                            element: <WarehousesPage/>
                        },
                        {
                            path: ":shopId/warehouses/create",
                            element: <WarehouseCreatePage/>
                        },
                        {
                            path: ":shopId/warehouses/:id",
                            element: <WarehouseDetailPage/>
                        },
                        {
                            path: ":shopId/warehouses/:id/edit",
                            element: <WarehouseEditPage/>
                        },
                        {
                            path: ":shopId/warehouses/:id/delete",
                            element: <WarehouseDeletePage/>
                        },
                        {
                            path: ":shopId/inventory",
                            element: <InventoryPage/>
                        },
                        {
                            path: ":shopId/inventory/:id",
                            element: <InventoryDetailPage/>
                        },
                        {
                            path: ":shopId/shipments",
                            element: <ShipmentsPage/>
                        },
                        {
                            path: ":shopId/shipments/:id",
                            element: <ShipmentDetailPage/>
                        }
                    ]
                }
            ]
        },
        {
            path: "*",
            element: <PageNotFound/>
        }
    ]
);