import { Spinner } from "@/components/ui/spinner.tsx";

type LoadingProps = {
    classNames?: string;
    title?: string;
    message?: string;
}

export default function Loading( { classNames, title, message }: LoadingProps ) {
    const classes = classNames || "flex flex-col items-center justify-center h-screen gap-3 **:tracking-tight"
    return (
        <div className={ classes }>
            <Spinner className="size-10"></Spinner>
            <div className="text-center leading-5">
                <h2 className="font-bold">{ title || "App is loading" }</h2>
                <small
                    className="text-stone-400">{ message || "Please do not refresh the page. Getting everything ready for\n" +
                    "                    you" }</small>
            </div>
        </div>
    )
}