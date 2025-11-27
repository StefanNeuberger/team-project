export default function PageNotFound() {

    return (
        <div className={ "flex-1 flex bg-accent flex-col gap-4 justify-center items-center" }>
            <h2 className={ "text-foreground font-bold text-2xl" }>404: Page not found</h2>
            <div className={ "w-full bg-accent rounded-full" }>
                <img src={ "/404.svg" } className={ "w-1/2 min-w-[300px] m-auto" } alt={ "" }/>
            </div>
        </div>
    )
}
