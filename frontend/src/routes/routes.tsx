import { createBrowserRouter } from "react-router-dom";
import RootLayout from "@/RootLayout.tsx";
import Home from "@/pages/Home.tsx";
import PageNotFound from "@/pages/PageNotFound.tsx";


export const router = createBrowserRouter( [
        {
            path: "/",
            element: <RootLayout/>,
            children: [
                {
                    index: true,
                    element: <Home/>
                },
                {
                    path: "*",
                    element: <PageNotFound/>
                }
            ]
        },
    ]
);