import {
    NavigationMenu,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
} from "@/components/ui/navigation-menu"
import { NavLink, useLocation, useParams } from "react-router-dom";

type NavigationLinkType = {
    name: string;
    href: string;
}


export default function BaseNavigation( { isMobile, toggleMenu }: Readonly<{
    isMobile?: boolean,
    toggleMenu?: () => void
}> ) {

    const { shopId } = useParams();

    const navigationLinks: NavigationLinkType[] = [
        { name: 'Home', href: `/` },
        { name: 'Items', href: `/items` },
        { name: 'Warehouses', href: `/shop/${ shopId }/warehouses` },
        { name: 'Inventory', href: `/shop/${ shopId }/inventory` },
        { name: 'Shipments', href: `/shop/${ shopId }/shipments` },
    ];

    const handleLinkClick = () => {
        if ( isMobile && toggleMenu ) {
            toggleMenu();
        }
    };

    const { pathname } = useLocation();

    const orientation = isMobile ? 'vertical' : 'horizontal';

    return (
        <NavigationMenu className={ "items-start" } orientation={ orientation }>
            <NavigationMenuList className={ "flex-col items-start md:flex-row" }>
                { navigationLinks.map( ( { name, href } ) => (
                    <NavigationMenuItem key={ name }>
                        <NavigationMenuLink data-active={ pathname === href }
                                            className={ "data-[active=true]:text-accent-foreground" } asChild>
                            <NavLink onClick={ handleLinkClick } to={ href }>
                                { name }
                            </NavLink>
                        </NavigationMenuLink>
                    </NavigationMenuItem>
                ) ) }
            </NavigationMenuList>
        </NavigationMenu>
    )
}
