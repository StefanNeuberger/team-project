import { useEffect, useState } from "react";

interface UseIsMobileReturn {
    isMobile: boolean;
    isLoading: boolean;
}

const mobileBreakpoint = 767; // pixels

export const useIsMobile = (): UseIsMobileReturn => {
    const [ isMobile, setIsMobile ] = useState( false );
    const [ isLoading, setIsLoading ] = useState( true );

    useEffect( () => {
        const checkIsMobile = () => {
            // Check using media query
            const mediaQuery = globalThis.matchMedia( `(max-width: ${ mobileBreakpoint }px)` );

            // Check using user agent (additional detection)
            const userAgent = navigator.userAgent.toLowerCase();
            const mobileKeywords = [
                'android', 'webos', 'iphone', 'ipad', 'ipod',
                'blackberry', 'windows phone', 'mobile'
            ];

            const isMobileUA = mobileKeywords.some( keyword =>
                userAgent.includes( keyword )
            );

            // Combine both checks - prioritize media query but consider user agent
            const isMobileDevice = mediaQuery.matches ||
                ( isMobileUA && globalThis.innerWidth <= mobileBreakpoint );

            setIsMobile( isMobileDevice );
            setIsLoading( false );
        };

        // Initial check
        checkIsMobile();

        // Listen for media query changes
        const mediaQuery = globalThis.matchMedia( `(max-width: ${ mobileBreakpoint }px)` );
        const handleChange = () => checkIsMobile();

        if ( mediaQuery.addEventListener ) {
            mediaQuery.addEventListener( 'change', handleChange );
        } else {
            // Fallback for older browsers
            mediaQuery.addListener( handleChange );
        }

        // Listen for globalThis resize
        globalThis.addEventListener( 'resize', checkIsMobile );

        return () => {
            if ( mediaQuery.removeEventListener ) {
                mediaQuery.removeEventListener( 'change', handleChange );
            } else {
                mediaQuery.removeListener( handleChange );
            }
            globalThis.removeEventListener( 'resize', checkIsMobile );
        };
    }, [] );

    return {
        isMobile,
        isLoading,
    };
};

export default useIsMobile;