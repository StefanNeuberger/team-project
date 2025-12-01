import { createBrowserRouter } from "react-router-dom";
import { ErrorBoundary } from "@/ErrorBoundary.tsx";
import RootLayout from "@/layouts/RootLayout.tsx";
import PageNotFound from "@/pages/PageNotFound.tsx";
import ErrorPage from "@/pages/Error.tsx";
import ShopLayout from "@/layouts/ShopLayout.tsx";
import WarehousesPage from "@/pages/WarehousesPage.tsx";
import ItemsPage from "@/pages/ItemsPage.tsx";
import InventoryPage from "@/pages/InventoryPage.tsx";
import ItemDetailPage from "@/pages/ItemDetailPage.tsx";
import WarehouseDetailPage from "@/pages/WarehouseDetailPage.tsx";
import InventoryDetailPage from "@/pages/InventoryDetailPage.tsx";
import ShipmentsPage from "@/pages/ShipmentsPage.tsx";
import ShipmentDetailPage from "@/pages/ShipmentDetailPage.tsx";

import { ShopsPage } from "@/pages/ShopsPage";

export const router = createBrowserRouter( [
        {
            path: "/",
            element: <RootLayout/>,
            errorElement: <ErrorPage/>,
            ErrorBoundary: ErrorBoundary,
            children: [
                {
                    path: "shop",
                    element: <ShopLayout/>,
                    children: [
                        {
                            path: ":shopId",
                            element: <ShopsPage/>,
                        },
                        {
                            path: ":shopId/items",
                            element: <ItemsPage/>
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
                            path: ":shopId/warehouses/:id",
                            element: <WarehouseDetailPage/>
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
                },
                {
                    path: "items",
                    element: <ItemsPage/>
                }
            ]
        },
        {
            path: "*",
            element: <PageNotFound/>
        }
    ]
);